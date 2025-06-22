import {
  addEdge,
  MarkerType,
  ReactFlowProvider,
  type Edge,
} from "@xyflow/react";
import Board from "./Board";
import NodeList from "./NodeList";
import { DnDProvider } from "./DnDContext";
import useInitStore from "./store.init";
import { useEffect } from "react";
import useStore from "./store.flow";
import { ulid } from "ulid";
import type { FlowNode } from "./types";
import _ from "lodash";

type FlowCanvasProps = {
  row?: FlowRow | null | undefined;
};

export default function FlowCanvas({ row }: FlowCanvasProps) {
  const { load, state } = useInitStore((s) => s);

  useEffect(() => {
    load();
  });

  switch (state) {
    case "init":
      return <div>Loading...</div>;
    case "loading":
      return <div>Loading...</div>;
    case "error":
      return <div>Error</div>;
    case "done":
      return <DoneState row={row} />;
    default:
      throw new Error("Invalid state");
  }
}

function DoneState({ row }: FlowCanvasProps) {
  const { metaOfNodes } = useInitStore((s) => s);
  const { setNodes, setEdges, setDedupKey, setMode, setFlowDescription } =
    useStore((s) => s);

  const nameToMeta = _.keyBy(metaOfNodes, "name");

  useEffect(() => {
    if (row) {
      setMode("update");
      setDedupKey(row.id + "");
      setFlowDescription(row.description);

      const flow = JSON.parse(row.data) as { flowNodes: FlowNode[] };
      setNodes(
        flow.flowNodes.map((it) => ({
          id: it.id,
          type: "yaffNode",
          position: { x: it.px ?? 0, y: it.py ?? 0 },
          data: {
            input:
              it.assignExpressions?.reduce((acc, cur) => {
                acc[cur.inputName] = cur.expression;
                return acc;
              }, {} as Record<string, string>) ?? {},
            ref: it.ref,
            description: it.description,
            meta: nameToMeta[it.name],
          },
        }))
      );

      let edges: Edge[] = [];
      for (const it of flow.flowNodes) {
        if (it.next) {
          for (const target of it.next) {
            edges = addEdge(
              {
                id: `${it.id}-${target}`,
                markerEnd: {
                  type: MarkerType.ArrowClosed,
                  width: 32,
                  height: 24,
                  strokeWidth: 2,
                  color: "black",
                },
                source: it.id,
                target,
              },
              edges
            );
          }
        }
      }
      setEdges(edges);
    } else {
      setMode("create");
      const startMeta = metaOfNodes.find((m) => m.name === "control.start");
      const startNode = {
        id: "start",
        type: "yaffNode",
        position: { x: 160, y: 300 },
        data: { meta: startMeta },
      };
      setDedupKey(ulid());
      setNodes(startMeta ? [startNode] : []);
    }
  }, [metaOfNodes]);

  return (
    <ReactFlowProvider>
      <DnDProvider>
        <div className="flex w-screen h-screen">
          <div className="w-64 overflow-auto">
            <NodeList />
          </div>
          <div className="flex-1 h-full">
            <Board />
          </div>
        </div>
      </DnDProvider>
    </ReactFlowProvider>
  );
}
