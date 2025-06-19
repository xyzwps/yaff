import { Handle, Position, type NodeProps } from "@xyflow/react";
import useStore from "./store";
import { type YaffNodeData } from "./types";
export default function YaffNode({ id, data }: NodeProps<YaffNodeData>) {
  const updateYaffNodeRef = useStore((state) => state.updateYaffNodeRef);
  const updateYaffInput = useStore((state) => state.updateYaffInput);
  const updateYaffNodeDescription = useStore(
    (state) => state.updateYaffNodeDescription
  );

  const {
    meta: { inputs, name, description, output },
  } = data;

  const noRef =
    name === "control.start" ||
    name === "control.end" ||
    name === "control.case" ||
    name === "control.default" ||
    name === "yaff.noop" ||
    output === null || output === undefined;

  const noDescription =
    name === "control.start" ||
    name === "control.end" ||
    name === "control.case" ||
    name === "yaff.noop";

  const width = noDescription ? "w-36" : "w-80";

  return (
    <div className={`card rounded-md p-0 ${width} shadow-sm`}>
      <Handle type="target" position={Position.Left} />
      <h2 className="card-title rounded-t-md p-2 text-sm bg-linear-to-b from-indigo-200 to-indigo-50">
        #{name}
      </h2>
      <div className="card-body p-2 bg-indigo-50">
        {noRef || (
          <div className="inline-flex items-center gap-2">
            <div className="w-10">引用:</div>
            <input
              value={data.ref}
              placeholder="用于后续节点使用本节点的输出"
              className="input input-sm"
              onChange={(e) => updateYaffNodeRef(id, e.target.value)}
            />
          </div>
        )}
        {noDescription || (
          <div className="inline-flex items-center gap-2">
            <div className="w-10">描述:</div>
            <input
              placeholder="描述此节点的用途"
              className="input input-sm"
              value={data.description}
              onChange={(e) => updateYaffNodeDescription(id, e.target.value)}
            />
          </div>
        )}
        {inputs && inputs.length > 0 && (
          <div>
            {inputs.map((input) => (
              <div key={input.name}>
                <div className="inline-flex items-center gap-2">
                  <div className="w-10">输入:</div> {input.name}
                  <small className="font-mono text-indigo-500">
                    {input.type}
                  </small>
                </div>
                <input
                  placeholder={input.name}
                  className="input input-sm"
                  value={data.input[input.name]}
                  onChange={(e) =>
                    updateYaffInput(id, input.name, e.target.value)
                  }
                />
              </div>
            ))}
          </div>
        )}

        {description && (
          <div className="text-xs text-slate-600">节点用途: {description}</div>
        )}
      </div>
      <Handle type="source" position={Position.Right} />
    </div>
  );
}
