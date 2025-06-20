import { createPortal } from "react-dom";
import useStore from "./store";
import { useState } from "react";
import type { NodeProps } from "@xyflow/react";
import type { YaffNodeData } from "./types";
import { SHORT_PT } from "./dict";

export default function NodeDrawer() {
  const { selectedNode } = useStore((s) => s);
  if (!selectedNode) {
    return null;
  }
  return <NodeDrawer0 key={selectedNode.id} {...selectedNode} />;
}

const NO_REF_NODE_NAMES: Record<string, boolean> = {
  "control.start": true,
  "control.end": true,
  "control.case": true,
  "control.default": true,
  "yaff.noop": true,
};

const shouldNoRef = ({ name, output }: NodeMetaData) => {
  return NO_REF_NODE_NAMES[name] || output === null || output === undefined;
};

function NodeDrawer0({ id, data }: NodeProps<YaffNodeData>) {
  const {
    showNodeEditor,
    setShowNodeEditor,
    updateYaffNodeRef,
    updateYaffNodeDescription,
    updateYaffInput,
  } = useStore((s) => s);

  const [ref, setRef] = useState(data.ref);
  const [description, setDescription] = useState(data.description);
  const [input, setInput] = useState(data.input);
  const inputs = data.meta.inputs;

  const noRef = shouldNoRef(data.meta);

  return (
    <>
      {createPortal(
        <div
          key={id}
          className={`card w-80 bg-indigo-50 p-2 shadow absolute top-2 right-2 ${
            showNodeEditor ? "" : "hidden"
          }`}
        >
          <h2 className="card-title mb-2 p-2 inline-flex justify-between items-start">
            <span>#{data.meta.name}</span>
            <span
              onClick={() => setShowNodeEditor(false)}
              className="cursor-pointer"
            >
              ✖
            </span>
          </h2>
          <div
            style={{ height: "calc(100vh - 240px)" }}
            className="overflow-y-auto p-2"
          >
            {noRef || (
              <fieldset className="fieldset">
                {/* TODO: 格式校验 */}
                <legend className="fieldset-legend">输出引用:</legend>
                <input
                  type="text"
                  className="input input-sm"
                  value={ref}
                  placeholder="用于后续节点使用本节点的输出"
                  onChange={(e) => {
                    const v = e.target.value;
                    setRef(v);
                    updateYaffNodeRef(id, v);
                  }}
                />
              </fieldset>
            )}
            {inputs && inputs.length > 0 && (
              <div>
                {inputs.map((it) => (
                  <fieldset key={it.name} className="fieldset">
                    {/* TODO: 支持 js 和常量 */}
                    <legend className="fieldset-legend gap-0">
                      输入 - let {it.name} :&nbsp;
                      <span className="text-indigo-500">
                        {SHORT_PT[it.type]}
                      </span>
                      &nbsp;=
                    </legend>
                    <input
                      placeholder={it.name}
                      className="input input-sm"
                      value={input[it.name]}
                      onChange={(e) => {
                        const v = e.target.value;
                        setInput((prev) => ({
                          ...prev,
                          [it.name]: v,
                        }));
                        updateYaffInput(id, it.name, v);
                      }}
                    />
                  </fieldset>
                ))}
              </div>
            )}
            <fieldset className="fieldset">
              <legend className="fieldset-legend">描述:</legend>
              <textarea
                className="textarea h-24"
                placeholder="描述此节点的用途"
                value={description}
                onChange={(e) => {
                  const d = e.target.value;
                  setDescription(d);
                  updateYaffNodeDescription(id, d);
                }}
              />
              <div className="label">备注这个节点的作用</div>
            </fieldset>
          </div>
        </div>,
        document.body
      )}
    </>
  );
}
