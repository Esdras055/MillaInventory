import { Navigate, Outlet } from "react-router-dom";
import useAuth from "../hooks/useAuth";

function RoleRoute({ allowedRoles }) {
  const { user } = useAuth();
  const userRoles = user?.roles || [];
  const canAccess = allowedRoles.some((role) => userRoles.includes(role));

  if (!canAccess) {
    return <Navigate to="/dashboard" replace />;
  }

  return <Outlet />;
}

export default RoleRoute;
