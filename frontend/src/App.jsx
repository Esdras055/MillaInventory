import { Navigate, Route, Routes } from "react-router-dom";
import AppLayout from "./components/AppLayout";
import ProtectedRoute from "./routes/ProtectedRoute";
import DashboardPage from "./pages/DashboardPage";
import LoginPage from "./pages/LoginPage";
import ModulePage from "./pages/ModulePage";
import ReportesPage from "./pages/ReportesPage";
import "./App.css";

function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<AppLayout />}>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route
            path="/productos"
            element={
              <ModulePage
                title="Productos"
                description="Gestion de productos del inventario."
              />
            }
          />
          <Route
            path="/bodegas"
            element={
              <ModulePage title="Bodegas" description="Gestion de bodegas disponibles." />
            }
          />
          <Route
            path="/categorias"
            element={
              <ModulePage
                title="Categorias"
                description="Gestion de categorias de productos."
              />
            }
          />
          <Route
            path="/marcas"
            element={<ModulePage title="Marcas" description="Gestion de marcas." />}
          />
          <Route
            path="/proveedores"
            element={
              <ModulePage
                title="Proveedores"
                description="Gestion de proveedores del inventario."
              />
            }
          />
          <Route
            path="/entradas"
            element={
              <ModulePage
                title="Entradas"
                description="Registro y consulta de entradas de inventario."
              />
            }
          />
          <Route
            path="/salidas"
            element={
              <ModulePage
                title="Salidas"
                description="Registro y consulta de salidas de inventario."
              />
            }
          />
          <Route path="/reportes" element={<ReportesPage />} />
        </Route>
      </Route>
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
}

export default App;
