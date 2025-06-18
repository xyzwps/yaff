import React, { useCallback } from "react";
import styles from "./CommonNode.module.css";
import type { Node } from "@xyflow/react";

export type CommonNodeProps = Node<{ value: string }>;

export default function CommonNode(props: CommonNodeProps) {
  const onChange = useCallback((evt: React.ChangeEvent<HTMLInputElement>) => {
    console.log(evt.target.value);
  }, []);

  console.log("props", props);

  return (
    <div className={styles.text_updater_node}>
      <div>
        <label htmlFor="text">Text:</label>
        <input
          id="text"
          name="text"
          value={props.data.value}
          onChange={onChange}
          className="nodrag"
        />
      </div>
    </div>
  );
}
