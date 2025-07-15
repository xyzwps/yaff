import type { JSONSchema } from "json-schema-typed";

const BASE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

const base62 = (x: number) => {
  let result = "";
  let c = x;
  while (c >= BASE.length) {
    const mod = c % BASE.length;
    result = BASE[mod] + result;
    c = Math.floor(c / BASE.length);
  }

  result = BASE[c] + result;
  return result;
};

export const tsId = () => base62(Date.now());

export const jsonSchemaType = (schema: JSONSchema): string => {
  if (!schema) {
    return "";
  }

  // @ts-ignore
  if ("type" in schema) {
    // @ts-ignore
    return schema.type;
  }

  return "TODO: object";
};
