import { Link } from "raviger";

export default function FlowListPage() {
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
                <th></th>
                <th>Name</th>
                <th>Job</th>
                <th>Favorite Color</th>
              </tr>
            </thead>
            <tbody>
              {/* row 1 */}
              <tr className="hover:bg-indigo-200">
                <th>1</th>
                <td>Cy Ganderton</td>
                <td>Quality Control Specialist</td>
                <td>Blue</td>
              </tr>
              {/* row 2 */}
              <tr className="hover:bg-indigo-200">
                <th>2</th>
                <td>Hart Hagerty</td>
                <td>Desktop Support Technician</td>
                <td>Purple</td>
              </tr>
              {/* row 3 */}
              <tr className="hover:bg-indigo-200">
                <th>3</th>
                <td>Brice Swyre</td>
                <td>Tax Accountant</td>
                <td>Red</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
