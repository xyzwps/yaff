import { useEffect } from "react";
import FlowCanvas from "../../components/FlowCanvas";

export default function FlowCreatePage() {
  useEffect(() => {
    document.title = "Create Flow";
  }, []);
  return <FlowCanvas />;
}
