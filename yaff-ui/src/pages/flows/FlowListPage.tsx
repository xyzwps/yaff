import { Link, useNavigate } from "raviger";
import { useEffect, useState } from "react";
import { getAllFlows, deleteFlow } from "@/apis";
import _ from "lodash";
import type { FlowRow, Paged } from "@/types";
import Table from "@/components/ui/Table";

export default function FlowListPage() {
  const [paged, setPaged] = useState<Paged<FlowRow>>({
    page: 1,
    size: 10,
    total: 0,
    data: [],
  });
  const navigate = useNavigate();

  useEffect(() => {
    getAllFlows({ page: 1, size: 10 }).then((paged) => {
      setPaged(paged);
    });
  }, []);

  const handleDelete = async (id: number) => {
    await deleteFlow(id);
    setPaged((old) => ({
      page: old.page,
      size: old.size,
      total: old.total - 1,
      data: old.data.filter((flow) => flow.id !== id),
    }));
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
          <Table
            data={paged.data}
            columns={[
              { title: "ID", path: "id" },
              { title: "描述", path: "description" },
              { title: "创建时间", path: "createdAt" },
              { title: "更新时间", path: "updatedAt" },
              {
                title: "操作",
                path: "id",
                render: (row) => (
                  <span className="inline-flex gap-2">
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
                  </span>
                ),
              },
            ]}
          />
        </div>
      </div>
    </div>
  );
}
