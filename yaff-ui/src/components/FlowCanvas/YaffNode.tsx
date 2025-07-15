import { Handle, Position, type NodeProps } from "@xyflow/react";
import useStore from "./store.flow";
import { type YaffNodeData } from "./types";
import NodeIcon from "./NodeIcon";
import type { NodeInput, NodeOutput } from "../../types";

const TITLE_BG: Record<string, string> = {
  "control.start": "from-sky-200",
  "control.end": "from-rose-200",
  "control.case": "from-amber-200",
  "control.when": "from-lime-200",
  "control.default": "from-emerald-200",
};

export default function YaffNode(props: NodeProps<YaffNodeData>) {
  const { data } = props;
  const { setEditorMode, setSelectedNode } = useStore((s) => s);
  const { inputs, name, description, output } = data.meta;

  const zhai =
    name === "control.start" ||
    name === "control.end" ||
    name === "control.case" ||
    name === "yaff.noop";

  const width = zhai ? "w-36" : "w-80";

  const titleBg = TITLE_BG[name] || "from-indigo-200";

  return (
    <div
      className={`card rounded-md p-0 ${width} shadow-sm`}
      onClick={() => {
        setSelectedNode(JSON.parse(JSON.stringify(props)));
        setEditorMode("node");
      }}
    >
      <Handle type="target" position={Position.Left} />
      <h2
        className={`card-title rounded-t-md p-2 text-sm bg-linear-to-b ${titleBg} to-indigo-50`}
      >
        <NodeIcon name={name} size={16} />
        {name}
      </h2>
      <div className="card-body p-2 bg-indigo-50">
        {data.description && (
          <div className="text-xs text-slate-600">
            <span className="w-10 font-bold">描述:&nbsp;</span>
            {data.description}
          </div>
        )}
        {inputs && inputs.length > 0 && (
          <div className="inline-flex items-center gap-2">
            <div className="w-10">输入:</div>
            {inputs.map((input) => (
              <InputInfo key={input.name} {...input} />
            ))}
          </div>
        )}
        {output && (
          <div className="inline-flex items-center gap-2">
            <div className="w-10">输出:</div>
            <OutputInfo {...output} ref={data.ref} />
          </div>
        )}

        <div className="inline-flex justify-between items-center">
          {description ? (
            <div className="text-xs text-slate-600">{description}</div>
          ) : (
            <div />
          )}
        </div>
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  );
}

function InputInfo({ schema, name }: NodeInput) {
  return (
    <div className="badge badge-sm text-md">
      <small className="text-indigo-500">{schema.type}</small>
      {name}
    </div>
  );
}

function OutputInfo({
  schema,
  ref,
}: NodeOutput & { ref?: string | null | undefined }) {
  return (
    <div className="badge badge-sm text-md">
      <small className="text-indigo-500">{schema.type}</small>
      {ref}
    </div>
  );
}
