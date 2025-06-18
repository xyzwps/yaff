import { useEffect, useState } from "react";
import { getMetaData } from "../../apis";
import styles from "./index.module.css";
import { useDnD } from "../Board/DnDContext";

export default function NodeList() {
  const [nodes, setNodes] = useState<NodeMetaData[]>([]);

  const [_, setMetaData] = useDnD();

  const onDragStart = (
    event: React.DragEvent<HTMLDivElement>,
    meta: NodeMetaData
  ) => {
    setMetaData(meta);
    event.dataTransfer.effectAllowed = "move";
  };

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
        <NodeItem
          key={metaData.name}
          meta={metaData}
          onDragStart={(e) => onDragStart(e, metaData)}
        />
      ))}
    </div>
  );
}

function NodeItem({
  meta,
  onDragStart,
}: {
  meta: NodeMetaData;
  onDragStart: (e: React.DragEvent<HTMLDivElement>) => void;
}) {
  return (
    <div className={styles.node_item} onDragStart={onDragStart} draggable>
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
