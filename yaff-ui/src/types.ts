import type { JSONSchema } from "json-schema-typed";

export type NodeMetaData = {
  name: string;
  description?: string | null | undefined;
  inputs?: NodeInput[] | null | undefined;
  output?: NodeOutput | null | undefined;
};

export type NodeInput = {
  name: string;
  schema: JSONSchema;
};

export type NodeOutput = {
  schema: JSONSchema;
};

export type FlowDef = {
  id: number;
  description: string;
  data: string;
  createdAt: string;
  updatedAt: string;
};


export type Paged<T> = {
  page: number;
  size: number;
  total: number;
  data: T[];
}