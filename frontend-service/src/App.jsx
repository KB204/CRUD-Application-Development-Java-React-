import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import NavBar from "./components/Navbar/NavBar.jsx";
import Home from './pages/Home/Home.jsx';
import ListCategory from './pages/Categorie/ListCategory.jsx';
import Category from './pages/Categorie/Category.jsx';
import NewCategory from './components/Forms/NewCategory.jsx';
import Layout from './components/Layout/Layout.jsx';
import './App.css'
import Sidebar from "./components/Sidebar/SideBar.jsx";

function App() {
    return (
        <Router>
            <div className="min-h-screen bg-gray-50">
                <NavBar />
                <Sidebar />
                <div className="container mx-auto px-4 py-8">
                    <Routes>
                        <Route path="/" element={<Layout><Home /></Layout>} />
                        <Route path="/categories" element={<Layout><ListCategory /></Layout>} />
                        <Route path="/newCategorie" element={<Layout><NewCategory /></Layout>} />
                        <Route path="/categorie/:id" element={<Layout><Category /></Layout>} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App
