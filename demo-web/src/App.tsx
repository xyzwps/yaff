import { useRoutes } from "raviger";
import HomePage from "./pages/HomePage";
import FlowListPage from "./pages/flows/FlowListPage";
import FlowCreatePage from "./pages/flows/FlowCreatePage";
import FlowUpdatePage from "./pages/flows/FlowUpdatePage";

const routes = {
  "/": () => <HomePage />,
  "/flows": () => <FlowListPage />,
  "/create/flows": () => <FlowCreatePage />,
  "/update/flows/:id": ({ id }: { id: string }) => <FlowUpdatePage id={+id} />,
};

function App() {
  const route = useRoutes(routes);
  return route;
}

export default App;
