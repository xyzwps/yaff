import { useEffect, useState } from "react";
import { getFlow } from "@/apis/yaff";
import FlowCanvas from "@/components/FlowCanvas";
import type { FlowDef } from "@/types";

export default function FlowUpdatePage({ id }: { id: number }) {
  const [row, setRow] = useState<FlowDef | null>(null);

  useEffect(() => {
    getFlow(id)
      .then(setRow)
      .catch((err) => {
        console.log("getFlow error", id, err);
      });
  }, [id]);

  if (row == null) {
    return <div>Loading...</div>;
  }

  return <FlowCanvas row={row} />;
}
