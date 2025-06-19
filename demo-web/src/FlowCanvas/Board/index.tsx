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
import type { AppState } from "./types";
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
        id: "n" + Date.now(),
        type: "yaffNode",
        position,
        data: { description: "", input: {}, meta },
      };

      const newNodes = [...nodes, newNode];
      console.log("oldNodes", nodes);
      console.log("newNodes", newNodes);
      setNodes(newNodes);
    },
    [screenToFlowPosition, meta]
  );

  const handleSave = () => {
    console.log(nodes, edges);
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
