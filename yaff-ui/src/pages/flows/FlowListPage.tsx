import { Link, useNavigate } from "raviger";
import { useEffect, useState } from "react";
import { getAllFlows, deleteFlow, runFlow } from "@/apis/yaff";
import _ from "lodash";
import type { FlowDef, Paged } from "@/types";
import Table from "@/components/ui/Table";
import Modal from "@/components/ui/Modal";

function RunFlowButton({ id }: { id: number }) {
  const [loading, setLoading] = useState(false);
  const [visible, setVisible] = useState(false);

  const run = async () => {
    setLoading(true);
    const result = await runFlow(id);
    console.log(result);
    setLoading(false);
  };

  return (
    <>
      <button
        className="btn btn-xs"
        onClick={() => setVisible(true)}
        disabled={loading}
      >
        执行
      </button>
      <Modal
        visible={visible}
        title="执行"
        onVisibleChange={setVisible}
        actions={
          <>
            <button className="btn" onClick={() => setVisible(false)}>
              关闭
            </button>
            <button className="btn" onClick={() => run()}>
              执行
            </button>
          </>
        }
      >
        {loading ? "执行中..." : "待执行"}
      </Modal>
    </>
  );
}

export default function FlowListPage() {
  const [paged, setPaged] = useState<Paged<FlowDef>>({
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
                    <RunFlowButton id={row.id} />
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
