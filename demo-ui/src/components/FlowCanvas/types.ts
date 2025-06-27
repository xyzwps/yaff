import {
  type Edge,
  type Node,
  type OnNodesChange,
  type OnEdgesChange,
  type OnConnect,
  type NodeProps,
} from "@xyflow/react";

export type AppNode = Node;

type Mode = "create" | "update";

type EditorMode = "flow" | "node" | "none";

export type AppState = {
  mode: Mode;
  setMode: (mode: Mode) => void;
  editorMode: EditorMode;
  setEditorMode: (mode: EditorMode) => void;
  nodes: AppNode[];
  setNodes: (nodes: AppNode[]) => void;
  edges: Edge[];
  setEdges: (edges: Edge[]) => void;
  dedupKey: string;
  setDedupKey: (dedupKey: string) => void;
  selectedNode: NodeProps<YaffNodeData> | null;
  setSelectedNode: (node: NodeProps<YaffNodeData> | null) => void;
  flowDescription: string;
  setFlowDescription: (v: string) => void;
  onNodesChange: OnNodesChange<AppNode>;
  onEdgesChange: OnEdgesChange;
  onConnect: OnConnect;
  updateYaffNodeRef: (nodeId: string, yaffNodeRef: string) => void;
  updateYaffNodeDescription: (
    nodeId: string,
    yaffNodeDescription: string
  ) => void;
  updateYaffInput: (
    nodeId: string,
    yaffInputName: string,
    yaffInputValue: string
  ) => void;
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
    ref: string;
    description: string;
    meta: NodeMetaData;
  },
  "yaffNode"
>;

export type FlowNode = {
  id: string;
  ref?: string | null | undefined;
  description?: string | null | undefined;
  px?: number | null | undefined;
  py?: number | null | undefined;
  name: string;
  next?: string[] | null | undefined;
  assignExpressions?:
    | {
        expression: string;
        inputName: string;
        type: "javascript";
      }[]
    | null
    | undefined;
};
