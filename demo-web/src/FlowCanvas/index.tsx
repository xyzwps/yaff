import { ReactFlowProvider } from "@xyflow/react";
import Board from "./Board";
import NodeList from "./NodeList";
import styles from "./index.module.css";
import { DnDProvider } from "./Board/DnDContext";

export default function FlowCanvas() {
  return (
    <ReactFlowProvider>
      <DnDProvider>
        <div className={styles.flow_canvas}>
          <div className={styles.flow_canvas_left}>
            <NodeList />
          </div>
          <div>
            <Board />
          </div>
        </div>
      </DnDProvider>
    </ReactFlowProvider>
  );
}
