import { ReactFlow, Background, Controls } from "@xyflow/react";
import { useMemo } from "react";
import "@xyflow/react/dist/style.css";
import CommonNode from "../Node/CommonNode";

import { useShallow } from "zustand/react/shallow";

import useStore from "./store";
import type { AppState } from "./types";
import ColorChooserNode from "./ColorChooserNode";

const selector = (state: AppState) => ({
  nodes: state.nodes,
  edges: state.edges,
  onNodesChange: state.onNodesChange,
  onEdgesChange: state.onEdgesChange,
  onConnect: state.onConnect,
});

const nodeTypes = { commonNode: CommonNode, colorChooser: ColorChooserNode };

export default function Board() {
  const { nodes, edges, onNodesChange, onEdgesChange, onConnect } = useStore(
    useShallow(selector)
  );

  return (
    <div style={{ height: 720, width: 1080, background: "#fafafa" }}>
      <ReactFlow
        // @ts-ignore
        nodeTypes={nodeTypes}
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
        fitView
      >
        <Background />
        <Controls />
      </ReactFlow>
    </div>
  );
}
