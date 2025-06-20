import {
  ReactFlow,
  Background,
  Controls,
  MiniMap,
  useReactFlow,
  useViewport,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";

import { useShallow } from "zustand/react/shallow";

import useStore from "./store.flow";
import type { AppState, FlowNode, YaffNodeData } from "./types";
import YaffNode from "./YaffNode";
import { useCallback } from "react";
import { useDnD } from "./DnDContext";
import NodeDrawer from "./NodeDrawer";
import { tsId } from "./utils";

const selector = (state: AppState) => ({
  nodes: state.nodes,
  edges: state.edges,
  setNodes: state.setNodes,
  onNodesChange: state.onNodesChange,
  onEdgesChange: state.onEdgesChange,
  onConnect: state.onConnect,
});

const nodeTypes = {
  yaffNode: YaffNode,
};

export default function Board() {
  const { nodes, edges, setNodes, onNodesChange, onEdgesChange, onConnect } =
    useStore(useShallow(selector));
  // const reactFlowWrapper = useRef(null);
  const { screenToFlowPosition } = useReactFlow();
  const { zoom } = useViewport();
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
    const id =
      meta.name === "control.start"
        ? "start"
        : meta.name === "control.end"
        ? "end"
        : `n${tsId()}`;
    const newNode = {
      id,
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
      const nid = e.source;
      const node = idToNode[nid];
      node.next?.push(e.target);
    }

    alert(JSON.stringify(results, null, 2));
  };

  return (
    <div className="bg-pink-400 w-full h-full flex flex-col">
      <div className="h-12">
        <button className="btn" onClick={handleSave}>
          Save
        </button>{" "}
        <span>{Math.round(zoom * 100)}%</span>
        <NodeDrawer />
      </div>
      <div className="w-full h-full bg-slate-50">
        <ReactFlow
          // @ts-ignore
          nodeTypes={nodeTypes}
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          onConnect={onConnect}
          onDrop={onDrop}
          onDragOver={onDragOver}
          fitView
        >
          <Background />
          <Controls />
          <MiniMap />
        </ReactFlow>
      </div>
    </div>
  );
}
