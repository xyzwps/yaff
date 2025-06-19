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

import useStore from "./store";
import type { AppState, FlowNode, YaffNodeData } from "./types";
import YaffNode from "./YaffNode";
import { useCallback } from "react";
import { useDnD } from "./DnDContext";

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

  console.log("当前节点", nodes);

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
    const newNode = {
      id: "n" + Date.now(),
      type: "yaffNode",
      position,
      data: { description: "", input: {}, meta },
    };

    const newNodes = [...nodes, newNode];
    console.log("旧节点", nodes);
    console.log("新节点", newNodes);
    setNodes(newNodes);
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

    console.log("results: ", JSON.stringify(results));
  };

  return (
    <div>
      <button className="btn" onClick={handleSave}>
        Save
      </button>{" "}
      <span>{Math.round(zoom * 100)}%</span>
      <div style={{ height: 720, width: 1080, background: "#fafafa" }}>
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
