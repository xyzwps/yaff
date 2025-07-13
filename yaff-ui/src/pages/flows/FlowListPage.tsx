import { Link, useNavigate } from "raviger";
import { useEffect, useState } from "react";
import { getAllFlows, deleteFlow } from "../../apis";
import _ from "lodash";

export default function FlowListPage() {
  const [rows, setRows] = useState<FlowRow[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    getAllFlows().then((rows) => {
      setRows(_.orderBy(rows, ["id"], ["desc"]));
    });
  }, []);

  const handleDelete = async (id: number) => {
    await deleteFlow(id);
    setRows((oldRows) => {
      const newRows = oldRows.filter((row) => row.id !== id);
      console.log(oldRows, newRows);
      return newRows;
    });
  };

  return (
    <div className="bg-indigo-100 m-0 min-w-full min-h-screen">
      <div className="container mx-auto">
        <h1 className="text-xl font-bold py-4">流程列表</h1>
        <div className="pb-4">
          <Link className="btn btn-sm" href="/create/flows">
            创建流程
          </Link>
        </div>
        <div className="overflow-x-auto rounded-box border border-base-content/5 bg-base-100 shadow">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>描述</th>
                <th>创建时间</th>
                <th>修改时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((row) => (
                <tr key={row.id} className="hover:bg-indigo-200">
                  <th>{row.id}</th>
                  <td>{row.description}</td>
                  <td>{row.createdAt}</td>
                  <td>{row.updatedAt}</td>
                  <td className="inline-flex gap-2">
                    <button
                      className="btn btn-xs btn-primary"
                      onClick={() => navigate(`/update/flows/${row.id}`)}
                    >
                      查看
                    </button>
                    <button
                      className="btn btn-xs btn-error text-base-100"
                      onClick={() => handleDelete(row.id)}
                    >
                      删除
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
