import { createContext, useContext, useState } from "react";
import type { NodeMetaData } from "@/types";

const DnDContext = createContext<
  [NodeMetaData | null, (m: NodeMetaData) => void]
>([null, (_: NodeMetaData) => {}]);

export const DnDProvider = ({ children }: { children: React.ReactNode }) => {
  const [meta, setMeta] = useState<NodeMetaData | null>(null);

  return (
    <DnDContext.Provider value={[meta, setMeta]}>
      {children}
    </DnDContext.Provider>
  );
};

export default DnDContext;

export const useDnD = () => {
  return useContext(DnDContext);
};
