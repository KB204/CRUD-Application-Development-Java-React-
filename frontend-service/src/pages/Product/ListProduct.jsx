import { DataGrid } from "@material-ui/data-grid";
import { DeleteOutline } from "@material-ui/icons";
import EditIcon from '@material-ui/icons/Edit';
import Swal from 'sweetalert2';
import { Link } from "react-router-dom";
import Loading from "../../components/Loading/Loading";
import "../List.css";
import {useProductMutations, useProducts} from "../../hooks/useProducts.js";

export default function ListProduct() {
    const {
        products,
        loading: isLoading,
        error,
        pagination,
        handlePageChange,
        handleFilterChange,
        refreshProducts
    } = useProducts();

    const {
        deleteProduct,
        loading: deleteLoading,
        error: deleteError
    } = useProductMutations();

    // Show confirmation dialog before deleting
    const showDeleteConfirmation = (product) => {
        Swal.fire({
            title: 'Are you sure?',
            text: `You are about to delete product ${product.name}.`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!',
            cancelButtonText: 'No, cancel!',
            reverseButtons: true,
        }).then((result) => {
            if (result.isConfirmed) {
                handleDeleteConfirm(product);
            }
        });
    };

    const handleDelete = (product) => {
        showDeleteConfirmation(product);
    };

    const handleDeleteConfirm = async (product) => {
        try {
            await deleteProduct(product.id);
            refreshProducts();

            Swal.fire({
                icon: 'success',
                title: 'Deleted!',
                text: `product ${product.name} has been deleted.`,
                timer: 1500
            });
        } catch (error) {
            console.error('Error deleting product:', error.message);
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: `An error has occurred: ${error.message}`,
            });

        }
    };

    const columns = [
        { field: "name", headerName: <strong>product</strong>, width: 500 },
        { field: "description", headerName: <strong>Description</strong>, width: 500 },
        { field: "price", headerName: <strong>Price</strong>, width: 500},
        {
            field: "action",
            headerName: <strong>Action</strong>,
            width: 150,
            renderCell: (params) => (
                <>
                    <Link>
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

            <Link to="/newProduct">
                <button className="userAddButton" disabled={isLoading || deleteLoading}>
                    Create
                </button>
            </Link>

            {isLoading ? (
                <Loading />
            ) : (
                <DataGrid
                    rows={products || []}
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
