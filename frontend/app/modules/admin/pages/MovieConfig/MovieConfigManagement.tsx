"use client";
import { useEffect, useState } from "react";
import type { FormEvent } from "react";
import { getErrorMessage } from "@/lib/apiClient";
import { categoryApiService, type Category } from "@/services/categoryApi";
import { Check, Edit, Loader2, Plus, Tags, Trash2, X } from "lucide-react";
import { toast } from "sonner";
import ConfirmDeleteDialog from "../../components/ConfirmDeleteDialog";


export default function MovieConfigManagement() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loaded, setLoaded] = useState(false);
  const [newName, setNewName] = useState("");
  const [creating, setCreating] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editingName, setEditingName] = useState("");
  const [toDelete, setToDelete] = useState<Category | null>(null);
  const [deleting, setDeleting] = useState(false);

  const [reloadCount, setReloadCount] = useState(0);

  useEffect(() => {
    let ignore = false;

    categoryApiService.getAll()
        .then((res) => {
          if (!ignore) setCategories(res);
        })
        .catch((error) => {
          if (!ignore) {
            toast.error(
                getErrorMessage(error, "Không thể tải thể loại")
            );
          }
        })
        .finally(() => {
          if (!ignore) setLoaded(true);
        });

    return () => {
      ignore = true;
    };
  }, [reloadCount]);

  const fetchCategories = () => setReloadCount((n) => n + 1);

  const handleCreate = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!newName.trim()) return;
    setCreating(true);
    try {
      await categoryApiService.create({ name: newName.trim() });
      toast.success("Tạo thể loại thành công");
      setNewName("");
      fetchCategories();
    } catch (error) {
      toast.error(getErrorMessage(error, "Không thể tạo thể loại"));
    } finally {
      setCreating(false);
    }
  };

  const handleUpdate = async (id: number) => {
    if (!editingName.trim()) return;
    try {
      await categoryApiService.update(id, { name: editingName.trim() });
      toast.success("Cập nhật thể loại thành công");
      setEditingId(null);
      fetchCategories();
    } catch (error) {
      toast.error(getErrorMessage(error, "Không thể cập nhật thể loại"));
    }
  };

  const handleDelete = async () => {
    if (!toDelete) return;
    setDeleting(true);
    try {
      await categoryApiService.remove(toDelete.id);
      toast.success("Xóa thể loại thành công");
      setToDelete(null);
      fetchCategories();
    } catch (error) {
      toast.error(getErrorMessage(error, "Không thể xóa thể loại"));
    } finally {
      setDeleting(false);
    }
  };

  return (
    <div className="mx-auto max-w-3xl space-y-6 p-6">
      <div className="flex items-center gap-3">
        <Tags className="h-7 w-7 text-indigo-600" aria-hidden />
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Thể loại phim</h1>
          <p className="text-sm text-gray-500">{categories.length} thể loại</p>
        </div>
      </div>

      <form onSubmit={handleCreate} className="flex gap-2 rounded-xl bg-white p-4 shadow-sm">
        <input
          aria-label="Tên thể loại mới"
          placeholder="Tên thể loại mới..."
          maxLength={50}
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
          className="flex-1 rounded-lg border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:outline-none"
        />
        <button type="submit" disabled={creating || !newName.trim()}
          className="flex items-center gap-2 rounded-lg bg-indigo-600 px-4 py-2 text-sm text-white hover:bg-indigo-700 disabled:opacity-50">
          {creating ? <Loader2 className="h-4 w-4 animate-spin" aria-hidden /> : <Plus className="h-4 w-4" aria-hidden />}
          Thêm
        </button>
      </form>

      <ul className="divide-y divide-gray-100 rounded-xl bg-white shadow-sm">
        {!loaded ? (
          <li className="py-10 text-center">
            <Loader2 className="mx-auto h-6 w-6 animate-spin text-indigo-600" aria-label="Đang tải" />
          </li>
        ) : categories.length === 0 ? (
          <li className="py-10 text-center text-sm text-gray-500">Chưa có thể loại nào</li>
        ) : categories.map((category) => (
          <li key={category.id} className="flex items-center gap-3 px-4 py-3">
            <span className="w-10 text-xs text-gray-400">#{category.id}</span>
            {editingId === category.id ? (
              <>
                <input
                  autoFocus
                  aria-label="Tên thể loại"
                  maxLength={50}
                  value={editingName}
                  onChange={(e) => setEditingName(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") handleUpdate(category.id);
                    if (e.key === "Escape") setEditingId(null);
                  }}
                  className="flex-1 rounded-lg border border-indigo-400 px-3 py-1.5 text-sm focus:outline-none"
                />
                <button type="button" aria-label="Lưu" onClick={() => handleUpdate(category.id)}
                  className="rounded p-1.5 text-green-600 hover:bg-green-50"><Check className="h-4 w-4" /></button>
                <button type="button" aria-label="Hủy" onClick={() => setEditingId(null)}
                  className="rounded p-1.5 text-gray-500 hover:bg-gray-100"><X className="h-4 w-4" /></button>
              </>
            ) : (
              <>
                <span className="flex-1 text-sm text-gray-900">{category.name}</span>
                <button type="button" aria-label={`Sửa ${category.name}`}
                  onClick={() => { setEditingId(category.id); setEditingName(category.name); }}
                  className="rounded p-1.5 text-blue-600 hover:bg-blue-50"><Edit className="h-4 w-4" /></button>
                <button type="button" aria-label={`Xóa ${category.name}`} onClick={() => setToDelete(category)}
                  className="rounded p-1.5 text-red-600 hover:bg-red-50"><Trash2 className="h-4 w-4" /></button>
              </>
            )}
          </li>
        ))}
      </ul>

      <ConfirmDeleteDialog
        open={toDelete !== null}
        title="Xóa thể loại"
        message={`Xóa thể loại "${toDelete?.name}"? Không thể xóa nếu đang có phim thuộc thể loại này.`}
        loading={deleting}
        onConfirm={handleDelete}
        onClose={() => setToDelete(null)}
      />
    </div>
  );
}
