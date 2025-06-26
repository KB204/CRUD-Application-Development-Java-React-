import React from "react";
import FeaturedInfo from "../../components/Featuredinfo/FeaturedInfo";
import Chart from "../../components/Charts/Chart";
import "./Home.css";
import { userData } from "../../dummyData";
import WidgetLg from "../../components/Widegetlg/WidegetLg";
import WidgetSm from "../../components/Widegetsm/WidegestSm";

export default function Home() {
    return (
        <div className="home">
            <FeaturedInfo />
            <Chart data={userData} title="User Analytics" grid dataKey="Active User"/>
            <div className="homeWidgets">
                <WidgetLg/>
                <WidgetSm />
            </div>
        </div>
    );
}