import { ReactFlowProvider } from "@xyflow/react";
import Board from "./Board";
import NodeList from "./NodeList";
import { DnDProvider } from "./Board/DnDContext";
import useInitStore from "./store.init";
import { useEffect } from "react";

export default function FlowCanvas() {
  const { load, state } = useInitStore((s) => s);

  useEffect(() => {
    load();
  });

  switch (state) {
    case "init":
      return <div>Loading...</div>;
    case "loading":
      return <div>Loading...</div>;
    case "error":
      return <div>Error</div>;
    case "done":
      return <DoneState />;
    default:
      throw new Error("Invalid state");
  }
}

function DoneState() {
  return (
    <ReactFlowProvider>
      <DnDProvider>
        <div className="flex w-screen h-screen">
          <div className="w-64 overflow-auto">
            <NodeList />
          </div>
          <div className="flex-1 h-full">
            <Board />
          </div>
        </div>
      </DnDProvider>
    </ReactFlowProvider>
  );
}
