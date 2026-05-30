import api from "./axiosConfig";

export function getUsuarios() {
  return api.get("/users");
}

export function createUsuario(values) {
  return api.post("/users", values);
}

export function updateUsuario(id, values) {
  return api.put(`/users/${id}`, values);
}

export function deleteUsuario(id) {
  return api.delete(`/users/${id}`);
}

export function getRoles() {
  return api.get("/roles");
}

export function getUsuarioRoles(id) {
  return api.get(`/users/${id}/roles`);
}

export function replaceUsuarioRoles(id, roleIds) {
  return api.put(`/users/${id}/roles`, { roleIds });
}
