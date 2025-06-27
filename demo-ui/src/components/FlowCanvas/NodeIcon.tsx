import { Pause, PlayOne, Puzzle, STurnUp, TreeDiagram } from "@icon-park/react";

type NodeIconProps = {
  name?: string | null | undefined;
  size?: number;
};

export default function NodeIcon({ name, size }: NodeIconProps) {
  const theSize = size || 18;

  if (name?.startsWith("control.")) {
    switch (name) {
      case "control.start":
        return <PlayOne theme="outline" size={theSize} fill="#333" />;
      case "control.end":
        return <Pause theme="outline" size={theSize} fill="#333" />;
      case "control.all":
      case "control.case":
        return <TreeDiagram theme="outline" size={theSize} fill="#333" />;
      default:
        return <STurnUp theme="outline" size={theSize} fill="#333" />;
    }
  }

  return <Puzzle theme="outline" size={theSize} fill="#333" />;
}
