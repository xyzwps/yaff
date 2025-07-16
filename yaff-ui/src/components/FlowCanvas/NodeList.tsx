import type { NodeMetaData } from "@/types";
import { useDnD } from "./DnDContext";
import NodeIcon from "./NodeIcon";
import useInitStore from "./store.init";

export default function NodeList() {
  const { metaOfNodes } = useInitStore((s) => s);

  const [_, setMetaData] = useDnD();

  const onDragStart = (
    event: React.DragEvent<HTMLDivElement>,
    meta: NodeMetaData
  ) => {
    setMetaData(meta);
    event.dataTransfer.effectAllowed = "move";
  };

  return (
    <div className="list bg-base-100 rounded-box shadow-md py-1">
      {metaOfNodes.map((metaData) => (
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
  const { name, description } = meta;

  return (
    <div
      className="list-row p-2 flex flex-col gap-2 bg-indigo-100 my-1 mx-2 hover:bg-indigo-200"
      onDragStart={onDragStart}
      draggable
    >
      <div className="inline-flex gap-2">
        <NodeIcon name={name} /> <code className="font-bold">#{name}</code>
      </div>
      <p className="text-xs">{description}</p>
    </div>
  );
}
