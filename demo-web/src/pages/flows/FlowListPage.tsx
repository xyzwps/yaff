import { Link } from "raviger";
import { useEffect, useState } from "react";
import { getAllFlows } from "../../apis";

export default function FlowListPage() {
  const [rows, setRows] = useState<FlowRow[]>([]);

  useEffect(() => {
    getAllFlows().then((rows) => {
      setRows(rows);
    });
  }, []);

  return (
    <div className="bg-indigo-100 m-0 min-w-full min-h-screen">
      <div className="container mx-auto">
        <h1 className="text-xl font-bold py-4">Flow List</h1>
        <div className="pb-4">
          <Link className="btn btn-sm" href="/create/flows">
            Create Flow
          </Link>
        </div>
        <div className="overflow-x-auto rounded-box border border-base-content/5 bg-base-100 shadow">
          <table className="table">
            {/* head */}
            <thead>
              <tr>
                <th>ID</th>
                <th>描述</th>
                <th>创建时间</th>
                <th>修改时间</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => (
                <tr key={row.id} className="hover:bg-indigo-200">
                  <th>{row.id}</th>
                  <td>{row.description}</td>
                  <td>{row.createdAt}</td>
                  <td>{row.updatedAt}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
