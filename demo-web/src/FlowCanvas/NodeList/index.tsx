import { useEffect, useState } from "react";
import { getMetaData } from "../../apis";
import styles from "./index.module.css";

export default function NodeList() {
  const [nodes, setNodes] = useState<NodeMetaData[]>([]);

  useEffect(() => {
    getMetaData()
      .then((res) => {
        res.sort((a, b) => a.name.localeCompare(b.name));
        setNodes(res);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);
  return (
    <div className={styles.node_list}>
      {nodes.map((metaData) => (
        <NodeItem key={metaData.name} meta={metaData} />
      ))}
    </div>
  );
}

function NodeItem({ meta }: { meta: NodeMetaData }) {
  return (
    <div className={styles.node_item}>
      <code style={{ fontWeight: "bold" }}>#{meta.name}</code>
      <p style={{ margin: 0, fontSize: "0.8rem" }}>{meta.description}</p>
      {meta.input.length > 0 && (
        <div>
          <div
            style={{ display: "inline-flex", gap: 4, alignItems: "baseline" }}
          >
            ▶
            {meta.input.map((input) => (
              <Param key={input.name} param={input} />
            ))}{" "}
          </div>
        </div>
      )}
      {meta.output.length > 0 && (
        <div>
          <div
            style={{ display: "inline-flex", gap: 4, alignItems: "baseline" }}
          >
            ◀
            {meta.output.map((output) => (
              <Param key={output.name} param={output} />
            ))}{" "}
          </div>
        </div>
      )}
    </div>
  );
}

function Param({ param }: { param: Parameter }) {
  return (
    <code
      style={{
        padding: 2,
        border: "1px solid #ccc",
        borderRadius: 4,
        fontSize: "75%",
      }}
    >
      {param.name}
    </code>
  );
}
