import { ReactFlow, Background, Controls, MiniMap } from "@xyflow/react";
import { useReactFlow, useViewport } from "@xyflow/react";
import "@xyflow/react/dist/style.css";

import { useShallow } from "zustand/react/shallow";

import useStore from "./store.flow";
import type { AppState, FlowNode, YaffNodeData } from "./types";
import YaffNode from "./YaffNode";
import { useCallback } from "react";
import { useDnD } from "./DnDContext";
import BoardDrawer from "./BoardDrawer";
import { tsId } from "./utils";
import { createFlow, updateFlow } from "../../apis";
import { useNavigate } from "raviger";

const selector = (state: AppState) => ({
  mode: state.mode,
  nodes: state.nodes,
  edges: state.edges,
  setNodes: state.setNodes,
  onNodesChange: state.onNodesChange,
  onEdgesChange: state.onEdgesChange,
  onConnect: state.onConnect,
  dedupKey: state.dedupKey,
  flowDescription: state.flowDescription,
  setEditorMode: state.setEditorMode,
});

const nodeTypes = {
  yaffNode: YaffNode,
};

export default function Board() {
  const {
    mode,
    nodes,
    edges,
    dedupKey,
    flowDescription,
    setNodes,
    onNodesChange,
    onEdgesChange,
    onConnect,
    setEditorMode,
  } = useStore(useShallow(selector));
  // const reactFlowWrapper = useRef(null);
  const { screenToFlowPosition } = useReactFlow();
  const viewport = useViewport();
  const navigate = useNavigate();
  const [meta] = useDnD();

  const onDragOver = useCallback((event: React.DragEvent) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
  }, []);

  const onDrop = (event: React.DragEvent) => {
    event.preventDefault();

    // check if the dropped element is valid
    if (!meta) {
      return;
    }

    // project was renamed to screenToFlowPosition
    // and you don't need to subtract the reactFlowBounds.left/top anymore
    // details: https://reactflow.dev/whats-new/2023-11-10
    const position = screenToFlowPosition({
      x: event.clientX,
      y: event.clientY,
    });

    switch (meta.name) {
      case "control.start":
        return;
      case "control.end":
        const hasEnd = nodes.filter((it) => it.id === "end").length > 0;
        if (hasEnd) return;
    }

    const newNode = {
      id: genId(meta.name),
      type: "yaffNode",
      position,
      data: { description: "", input: {}, meta },
    };

    setNodes([...nodes, newNode]);
  };

  const handleSave = () => {
    const results: FlowNode[] = [];

    for (const n of nodes) {
      const { data } = n as YaffNodeData;
      results.push({
        id: n.id,
        name: data.meta.name,
        ref: data.ref,
        description: data.description,
        px: n.position.x,
        py: n.position.y,
        next: [],
        assignExpressions: data.input
          ? Object.entries(data.input).map(([name, value]) => ({
              expression: value,
              inputName: name,
              type: "javascript",
            }))
          : undefined,
      });
    }

    const idToNode: Record<string, FlowNode> = {};
    for (const n of results) {
      idToNode[n.id] = n;
    }

    for (const e of edges) {
      const {source, target } =e
      const sourceNode = idToNode[source];
      const targetNode = idToNode[target];
      if (sourceNode && targetNode) {
        const next = sourceNode.next || []
        next.push(e.target)
        sourceNode.next = next
      }
    }

    mode === "create"
      ? createFlow({
          description: flowDescription,
          data: { flowNodes: results },
        })
      : updateFlow({
          id: +dedupKey,
          description: flowDescription,
          data: { flowNodes: results },
        })
          .then(() => {
            navigate("/flows");
          })
          .catch(() => {
            alert(`Failed to ${mode} flow`);
          });
  };

  const showFlowInfo = () => setEditorMode("flow");

  return (
    <div className="bg-base-400 w-full h-full flex flex-col">
      <div className="h-12 inline-flex items-center gap-2 px-2">
        <button className="btn btn-sm" onClick={handleSave}>
          保存
        </button>{" "}
        <button className="btn btn-sm" onClick={showFlowInfo}>
          流程信息
        </button>{" "}
        <span>{Math.round(viewport.zoom * 100)}%</span>
        <BoardDrawer />
      </div>
      <div className="w-full h-full bg-slate-50">
        <ReactFlow
          // @ts-ignore
          nodeTypes={nodeTypes}
          nodes={nodes}
          edges={edges}
          defaultViewport={{ zoom: 1, x: 0, y: 0 }}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          onConnect={onConnect}
          onDrop={onDrop}
          onDragOver={onDragOver}
        >
          <Background />
          <Controls />
          <MiniMap />
        </ReactFlow>
      </div>
    </div>
  );
}

function genId(metaName: string) {
  switch (metaName) {
    case "control.start":
      return "start";
    case "control.end":
      return "end";
    default:
      return `n${tsId()}`;
  }
}
