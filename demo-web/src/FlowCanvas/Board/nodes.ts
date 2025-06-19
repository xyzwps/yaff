import { type AppNode } from "./types";

export const initialNodes = [
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
