import {
  type Edge,
  type Node,
  type OnNodesChange,
  type OnEdgesChange,
  type OnConnect,
} from "@xyflow/react";

export type AppNode = Node;

export type AppState = {
  nodes: AppNode[];
  edges: Edge[];
  onNodesChange: OnNodesChange<AppNode>;
  onEdgesChange: OnEdgesChange;
  onConnect: OnConnect;
  updateNodeColor: (nodeId: string, color: string) => void;
  updateYaffNodeId: (nodeId: string, yaffNodeId: string) => void;
  updateYaffNodeDescription: (
    nodeId: string,
    yaffNodeDescription: string
  ) => void;
  updateYaffInput: (
    nodeId: string,
    yaffInputName: string,
    yaffInputValue: string
  ) => void;
  setNodes: (nodes: AppNode[]) => void;
  setEdges: (edges: Edge[]) => void;
};

export type ColorNode = Node<
  {
    color: string;
  },
  "colorChooser"
>;

export type YaffNodeData = Node<
  {
    input: Record<string, string>;
    id: string;
    description: string;
    meta: NodeMetaData;
  },
  "yaffNode"
>;
