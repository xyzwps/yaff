import { type AppNode } from "./types";

export const initialNodes = [
  {
    id: "start",
    type: "yaffNode",
    position: { x: 160, y: 300 },
    data: {
      meta: { name: "control.start", description: "开始节点" },
    },
  },
] as AppNode[];
