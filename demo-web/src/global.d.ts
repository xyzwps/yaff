type NodeMetaData = {
  name: string
  description: string
  input: Parameter[]
  output: Parameter[]
}

type Parameter = {
  name: string
  type: "INT" | "FLOAT" | "STRING" | "BOOL"
}