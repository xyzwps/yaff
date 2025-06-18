import { type AppNode } from "./types";

export const initialNodes = [
  {
    id: "1",
    type: "colorChooser",
    data: { color: "#4FD1C5" },
    position: { x: 250, y: 25 },
  },

  {
    id: "2",
    type: "colorChooser",
    data: { color: "#F6E05E" },
    position: { x: 100, y: 125 },
  },
  {
    id: "3",
    type: "colorChooser",
    data: { color: "#B794F4" },
    position: { x: 250, y: 250 },
  },
  {
    id: "xx",
    type: "yaffNode",
    position: { x: 400, y: 300 },
    data: {
      id: "xxxx",
      description: "",
      input: { min: "", max: "" },
      meta: {
        name: "demo.rng",
        input: [
          { name: "min", type: "FLOAT" },
          { name: "max", type: "FLOAT" },
        ],
        output: [{ name: "result", type: "FLOAT" }],
        description: "生成一个随机数",
      },
    },
  },
] as AppNode[];
