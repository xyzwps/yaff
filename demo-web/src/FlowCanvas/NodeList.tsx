import { useEffect, useState } from "react";
import { getMetaData } from "../apis";
import { useDnD } from "./Board/DnDContext";

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
    <div className="list bg-base-100 rounded-box shadow-md">
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

type NodeItemProps = {
  meta: NodeMetaData;
  onDragStart: (e: React.DragEvent<HTMLDivElement>) => void;
};

function NodeItem({ meta, onDragStart }: NodeItemProps) {
  const { inputs, output, name, description } = meta;

  return (
    <div
      className="list-row p-2 flex flex-col gap-2 hover:bg-indigo-200"
      onDragStart={onDragStart}
      draggable
    >
      <div>
        <code className="font-bold">#{name}</code>
      </div>
      <p className="text-xs">{description}</p>
      {inputs && inputs.length > 0 && (
        <div className="inline-flex gap-2 align-baseline">
          ▶
          {inputs.map((input) => (
            <span className="badge badge-outline" key={input.name}>
              {input.name}
            </span>
          ))}
        </div>
      )}
      {output && (
        <div className="inline-flex gap-2 align-baseline">◀ {output.type}</div>
      )}
    </div>
  );
}
