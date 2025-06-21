import { create } from "zustand";
import {
  addEdge,
  applyNodeChanges,
  applyEdgeChanges,
  MarkerType,
} from "@xyflow/react";

import { type AppState } from "./types";

// this is our useStore hook that we can use in our components to get parts of the store and call actions
const useStore = create<AppState>((set, get) => ({
  nodes: [],
  edges: [],
  dedupKey: "",
  setDedupKey: (dedupKey) => set({ dedupKey }),
  showNodeEditor: false,
  setShowNodeEditor: (show) => set({ showNodeEditor: show }),
  selectedNode: null,
  setSelectedNode: (node) => set({ selectedNode: node }),
  setNodes: (nodes) => set({ nodes }),
  setEdges: (edges) => set({ edges }),
  onNodesChange: (changes) => {
    set({ nodes: applyNodeChanges(changes, get().nodes) });
  },
  onEdgesChange: (changes) => {
    set({ edges: applyEdgeChanges(changes, get().edges) });
  },
  onConnect: (connection) => {
    set({
      edges: addEdge(
        {
          markerEnd: {
            type: MarkerType.ArrowClosed,
            width: 32,
            height: 24,
            strokeWidth: 2,
            color: "black",
          },
          ...connection,
        },
        get().edges
      ),
    });
  },
  updateYaffNodeRef: (nodeId: string, yaffNodeRef: string) => {
    set({
      nodes: get().nodes.map((node) => {
        if (node.id === nodeId) {
          // it's important to create a new object here, to inform React Flow about the changes
          return { ...node, data: { ...node.data, ref: yaffNodeRef } };
        }
        return node;
      }),
    });
  },
  updateYaffNodeDescription: (nodeId: string, yaffNodeDescription: string) => {
    set({
      nodes: get().nodes.map((node) => {
        if (node.id === nodeId) {
          // it's important to create a new object here, to inform React Flow about the changes
          return {
            ...node,
            data: { ...node.data, description: yaffNodeDescription },
          };
        }
        return node;
      }),
    });
  },
  updateYaffInput: (
    nodeId: string,
    yaffInputName: string,
    yaffInputValue: string
  ) => {
    set({
      nodes: get().nodes.map((node) => {
        if (node.id === nodeId) {
          const inputs = node.data.input as {};
          return {
            ...node,
            data: {
              ...node.data,
              input: { ...inputs, [yaffInputName]: yaffInputValue },
            },
          };
        }
        return node;
      }),
    });
  },
}));

export default useStore;
