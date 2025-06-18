import {
  ReactFlow,
  Background,
  Controls,
  MiniMap,
  useReactFlow,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";
import CommonNode from "../Node/CommonNode";

import { useShallow } from "zustand/react/shallow";

import useStore from "./store";
import type { AppNode, AppState } from "./types";
import ColorChooserNode from "./ColorChooserNode";
import YaffNode from "./YaffNode";
import { useCallback, useRef } from "react";
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
  commonNode: CommonNode,
  colorChooser: ColorChooserNode,
  yaffNode: YaffNode,
};

export default function Board() {
  const { nodes, edges, setNodes, onNodesChange, onEdgesChange, onConnect } =
    useStore(useShallow(selector));
  const reactFlowWrapper = useRef(null);
  const { screenToFlowPosition } = useReactFlow();
  const [meta] = useDnD();

  const onDragOver = useCallback((event: React.DragEvent) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
  }, []);

  const onDrop = useCallback(
    (event: React.DragEvent) => {
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
        id: "xx" + Date.now(),
        type: "yaffNode",
        position,
        data: {
          id: "xxxx",
          description: "",
          input: {},
          meta,
        },
      };

      setNodes(nodes.concat(newNode));
    },
    [screenToFlowPosition, meta]
  );

  return (
    <div
      style={{ height: 720, width: 1080, background: "#fafafa" }}
      ref={reactFlowWrapper}
    >
      <ReactFlow
        // @ts-ignore
        nodeTypes={nodeTypes}
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
        zoomOnScroll={false}
        zoomOnPinch={false}
        zoomOnDoubleClick={false}
        onDrop={onDrop}
        onDragOver={onDragOver}
        fitView
      >
        <Background />
        <Controls />
        <MiniMap />
      </ReactFlow>
    </div>
  );
}
