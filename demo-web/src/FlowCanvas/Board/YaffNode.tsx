import { Handle, Position, type NodeProps } from "@xyflow/react";
import useStore from "./store";
import { type YaffNodeData } from "./types";
export default function YaffNode({ id, data }: NodeProps<YaffNodeData>) {
  const updateYaffNodeId = useStore((state) => state.updateYaffNodeId);
  const updateYaffInput = useStore((state) => state.updateYaffInput);
  const updateYaffNodeDescription = useStore(
    (state) => state.updateYaffNodeDescription
  );

  const { meta } = data;

  return (
    <div style={{ backgroundColor: "#CDCDFD", borderRadius: 10, padding: 8 }}>
      <Handle type="target" position={Position.Top} />
      <div>
        <div>
          ID <small>(用作其他节点引用此节点输出的变量名)</small>
        </div>
        <div>
          <input
            value={data.id}
            style={{ width: "100%" }}
            onChange={(e) => updateYaffNodeId(id, e.target.value)}
          />
        </div>
      </div>
      {meta.input && meta.input.length > 0 && (
        <div>
          {meta.input.map((input) => (
            <div key={input.name}>
              <div>
                输入: {input.name} | {input.type}
              </div>
              <input
                placeholder={input.name}
                style={{ width: "100%" }}
                value={data.input[input.name]}
                onChange={(e) =>
                  updateYaffInput(id, input.name, e.target.value)
                }
              />
            </div>
          ))}
        </div>
      )}
      <div>
        <div>描述：</div>
        <textarea
          placeholder="描述"
          style={{ width: "100%" }}
          value={data.description}
          onChange={(e) => updateYaffNodeDescription(id, e.target.value)}
        />
      </div>
      <div>
        输出:{" "}
        {meta.output.map((output) => (
          <code
            key={output.name}
            style={{
              padding: 2,
              borderRadius: 4,
              border: "1px solid #eee",
              marginRight: 4,
            }}
          >
            {output.name}
          </code>
        ))}
      </div>
      <div>节点类型: {meta.name}</div>
      <div>节点作用: {meta.description}</div>
      <Handle type="source" position={Position.Bottom} />
    </div>
  );
}
