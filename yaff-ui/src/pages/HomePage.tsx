import { useNavigate } from "raviger";

export default function HomePage() {
  const navigate = useNavigate();
  return (
    <div>
      <h1 className="text-center text-3xl font-bold mt-24">Flow Demo</h1>
      <div className="flex gap-2 justify-center mt-8">
        <button className="btn btn-info" onClick={() => navigate("/flows")}>
          Flow List
        </button>
        <button className="btn" onClick={() => navigate("/create/flows")}>
          Create a Flow
        </button>
      </div>
    </div>
  );
}
