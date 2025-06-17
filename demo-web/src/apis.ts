import ky from "ky";

export const getMetaData = async (): Promise<NodeMetaData[]> => {
  const response = await ky.get("/apis/yaff/metadata");
  return response.json();
};
