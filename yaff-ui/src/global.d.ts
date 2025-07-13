type NodeMetaData = {
  name: string;
  description?: string | null | undefined;
  inputs?: NodeInput[] | null | undefined;
  output?: NodeOutput | null | undefined;
};

type ParameterType = "INT" | "FLOAT" | "STRING" | "BOOL";

type NodeInput = {
  name: string;
  type: ParameterType;
};

type NodeOutput = {
  type: ParameterType;
};

type FlowRow = {
  id: number;
  dedupKey: string;
  description: string;
  data: string;
  createdAt: string;
  updatedAt: string;
};
