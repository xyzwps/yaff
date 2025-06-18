import {
  ReactFlow,
  Background,
  Controls,
  type Edge,
  type EdgeChange,
  type NodeChange,
  type Node,
  addEdge,
  type Connection,
} from "@xyflow/react";
import { useState, useCallback } from "react";
import { applyEdgeChanges, applyNodeChanges } from "@xyflow/react";
import "@xyflow/react/dist/style.css";

const initialNodes: Node[] = [
  {
    id: "1",
    position: { x: 0, y: 0 },
    data: { label: "Hello" },
    type: "input",
  },
  {
    id: "2",
    position: { x: 100, y: 100 },
    data: { label: "World" },
  },
];

const initialEdges: Edge[] = [];

export default function Board() {
  const [nodes, setNodes] = useState(initialNodes);
  const [edges, setEdges] = useState(initialEdges);

  const onNodesChange = useCallback(
    (changes: NodeChange<Node>[]) =>
      setNodes((nds) => applyNodeChanges(changes, nds)),
    []
  );
  const onEdgesChange = useCallback(
    (changes: EdgeChange<Edge>[]) =>
      setEdges((eds) => applyEdgeChanges(changes, eds)),
    []
  );

  const onConnect = useCallback(
    (params: Edge | Connection) => setEdges((eds) => addEdge(params, eds)),
    []
  );

  return (
    <div style={{ height: 720, width: 1080, background: "#fafafa" }}>
      <ReactFlow
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
