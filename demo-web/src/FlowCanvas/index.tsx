import Board from "./Board";
import NodeList from "./NodeList";
import styles from "./index.module.css";

export default function FlowCanvas() {
  return (
    <div className={styles.flow_canvas}>
      <div className={styles.flow_canvas_left}>
        <NodeList />
      </div>
      <div>
        <Board />
      </div>
    </div>
  );
}
