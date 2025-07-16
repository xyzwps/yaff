import { get } from "lodash";
import cn from "@/libs/cn";

export type TableColumn<T> = {
  title: React.ReactNode;
  path: string;
  render?: (data: T) => React.ReactNode;
};

export type TableProps<T> = {
  data: T[];
  columns: TableColumn<T>[];
  trClassName?: string | undefined;
} & React.DetailedHTMLProps<
  React.TableHTMLAttributes<HTMLTableElement>,
  HTMLTableElement
>;

export default function Table<T>({
  data,
  columns,
  trClassName,
  className,
  ...p
}: TableProps<T>) {
  return (
    <table className={cn("table", className)} {...p}>
      <thead>
        <tr>
          {columns.map((column, i) => (
            <th key={i}>{column.title}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {data.map((row, i) => (
          <tr key={i} className={trClassName}>
            {columns.map((column, j) => (
              <td key={j}>
                {column.render ? column.render(row) : get(row, column.path)}
              </td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
}
