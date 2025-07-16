import { create } from "zustand";
import { getMetaData } from "@/apis";
import type { NodeMetaData } from "@/types";

type InitState = {
  metaOfNodes: NodeMetaData[];
  state: "init" | "loading" | "done" | "error";
  load: () => Promise<void>;
};

// this is our useStore hook that we can use in our components to get parts of the store and call actions
const useInitStore = create<InitState>((set, get) => ({
  metaOfNodes: [],
  state: "init",
  load: async () => {
    const state = get().state;
    if (state === "loading" || state === "done") {
      return;
    }

    set({ state: "loading" });
    try {
      const metaOfNodes = await getMetaData();
      const controlMeta: NodeMetaData[] = [];
      const restMeta: NodeMetaData[] = [];
      for (const meta of metaOfNodes) {
        if (meta.name.startsWith("control.")) {
          controlMeta.push(meta);
        } else {
          restMeta.push(meta);
        }
      }

      set({ metaOfNodes: [...controlMeta, ...restMeta], state: "done" });
    } catch (e) {
      set({ state: "error" });
      console.error(e);
    }
  },
}));

export default useInitStore;
