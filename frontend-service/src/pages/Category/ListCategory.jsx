import { DataGrid } from "@material-ui/data-grid";
import { DeleteOutline } from "@material-ui/icons";
import EditIcon from '@material-ui/icons/Edit';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import Loading from "../../components/Loading/Loading";
import "../List.css";
import { useCategories, useCategoryMutations } from "../../hooks/useCategories";

export default function ListCategory() {
    const { 
        categories, 
        loading: isLoading, 
        error, 
        pagination,
        handlePageChange,
        handleFilterChange,
        refreshCategories
    } = useCategories();

    const {
        deleteCategory,
        loading: deleteLoading,
        error: deleteError
    } = useCategoryMutations();

    // Show confirmation dialog before deleting
    const showDeleteConfirmation = (category) => {
        Swal.fire({
            title: 'Are you sure?',
            text: `You are about to delete category ${category.name}.`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!',
            cancelButtonText: 'No, cancel!',
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                handleDeleteConfirm(category);
            }
        });
    };

    const handleDelete = (category) => {
        showDeleteConfirmation(category);
    };

    const handleDeleteConfirm = async (category) => {
        try {
            await deleteCategory(category.id);
            refreshCategories();

            Swal.fire({
                icon: 'success',
                title: 'Deleted!',
                text: `Category ${category.name} has been deleted.`,
                timer: 1500
            });
        } catch (error) {
            console.error('Error deleting category:', error.message);
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: `An error has occurred: ${error.message}`,
            });

        }
    };

    const columns = [
        { field: "name", headerName: <strong>Category</strong>, width: 900 },
        { field: "description", headerName: <strong>Description</strong>, width: 900 },
        {
            field: "action",
            headerName: <strong>Action</strong>,
            width: 150,
            renderCell: (params) => (
                <>
                    <Link to={`/categorie/${params.row.id}`}>
                        <EditIcon className="userListEdit" />
                    </Link>
                    <DeleteOutline className="userListDelete" onClick={() => handleDelete(params.row)} />
                </>
            ),
        },
    ];

    const displayError = error || deleteError;

    return (
        <div className="userList" style={{ height: '550px', width: '350%' }}>
        {displayError && (
                <div className="error-message" style={{ color: 'red', marginBottom: '10px' }}>
                    {displayError}
                </div>
            )}

            <Link to="/newCategorie">
                <button className="userAddButton" disabled={isLoading || deleteLoading}>
                    Create
                </button>
            </Link>

            {isLoading ? (
                <Loading />
            ) : (
                <DataGrid
                    rows={categories || []}
                    columns={columns}
                    pageSize={pagination.size}
                    page={pagination.page}
                    rowCount={pagination.totalElements}
                    paginationMode="server"
                    onPageChange={(newPage) => handlePageChange(newPage)}
                    rowsPerPageOptions={[8, 16, 24]}
                    onPageSizeChange={(newPageSize) => {
                        handlePageChange(0);
                        pagination.size = newPageSize;
                    }}
                    loading={isLoading}
                    disableSelectionOnClick
                    checkboxSelection
                />
            )}
        </div>
    );
}
