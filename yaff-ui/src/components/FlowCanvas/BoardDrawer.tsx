import { createPortal } from "react-dom";
import useStore from "./store.flow";
import { useState } from "react";
import type { NodeProps } from "@xyflow/react";
import type { YaffNodeData } from "./types";
import NodeIcon from "./NodeIcon";
import type { NodeMetaData } from "@/types";
import { jsonSchemaType } from "./utils";

export default function BoardDrawer() {
  const { selectedNode, editorMode } = useStore((s) => s);

  console.log("selectedNode", selectedNode);
  console.log("editorMode", editorMode);

  switch (editorMode) {
    case "node": {
      return !selectedNode ? null : (
        <Drawer
          key={selectedNode.id}
          title={
            <>
              <NodeIcon name={selectedNode.data.meta.name} />{" "}
              {selectedNode.data.meta.name}
            </>
          }
        >
          <NodeBody {...selectedNode} />
        </Drawer>
      );
    }
    case "flow": {
      return (
        <Drawer key="@flow" title="流程信息">
          <FlowBody />
        </Drawer>
      );
    }

    case "none":
      return null;
  }
}

function FlowBody() {
  const { flowDescription, setFlowDescription } = useStore((s) => s);
  const [value, setValue] = useState(flowDescription);

  return (
    <>
      <fieldset className="fieldset">
        <legend className="fieldset-legend">流程描述:</legend>
        <textarea
          className="textarea h-24"
          placeholder="描述此流程的用途"
          value={value}
          onChange={(e) => {
            const d = e.target.value;
            setValue(d);
            setFlowDescription(d);
          }}
        />
        <div className="label">备注这个节点的作用</div>
      </fieldset>
    </>
  );
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

function NodeBody({ id, data }: NodeProps<YaffNodeData>) {
  const { updateYaffNodeRef, updateYaffNodeDescription, updateYaffInput } =
    useStore((s) => s);

  const [ref, setRef] = useState(data.ref);
  const [refError, setRefError] = useState(false);
  const [description, setDescription] = useState(data.description);
  const [input, setInput] = useState(data.input);
  const inputs = data.meta.inputs;

  const noRef = shouldNoRef(data.meta);

  return (
    <>
      {noRef || (
        <fieldset className="fieldset">
          {/* TODO: 格式校验 */}
          <legend className="fieldset-legend">输出引用:</legend>
          <input
            type="text"
            className={`input input-sm ${
              refError ? "input-error" : "input-primary"
            }`}
            value={ref}
            placeholder="用于后续节点使用本节点的输出"
            onChange={(e) => {
              const v = e.target.value || "";
              if (v.length === 0 || /^[a-zA-Z_][a-zA-Z0-9_]*$/.test(v)) {
                setRefError(false);
                setRef(v);
                updateYaffNodeRef(id, v);
                return;
              }

              setRefError(true);
              setRef(v);
            }}
          />
          {refError && (
            <p className="label">
              引用应匹配 <code>^[a-zA-Z_][a-zA-Z0-9_]*$</code>
            </p>
          )}
        </fieldset>
      )}
      {inputs && inputs.length > 0 && (
        <div>
          {inputs.map((it) => (
            <fieldset key={it.name} className="fieldset">
              <legend className="fieldset-legend gap-0">
                输入 - let {it.name} :&nbsp;
                <span className="text-indigo-500">{jsonSchemaType(it.schema)}</span>
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
    </>
  );
}

type DrawerProps = {
  title: React.ReactNode;
  children: React.ReactNode;
};

function Drawer({ title, children }: DrawerProps) {
  const { editorMode, setEditorMode } = useStore((s) => s);

  return (
    <>
      {createPortal(
        <div
          className={`card w-80 bg-indigo-50 p-2 shadow absolute top-2 right-2 ${
            editorMode === "none" ? "hidden" : ""
          }`}
        >
          <h2 className="card-title mb-2 p-2 inline-flex justify-between items-start">
            <span className="inline-flex justify-left items-center gap-2">
              {title}
            </span>
            <span
              onClick={() => setEditorMode("none")}
              className="cursor-pointer"
            >
              ✖
            </span>
          </h2>
          <div
            style={{ height: "calc(100vh - 240px)" }}
            className="overflow-y-auto p-2"
          >
            {children}
          </div>
        </div>,
        document.body
      )}
    </>
  );
}
