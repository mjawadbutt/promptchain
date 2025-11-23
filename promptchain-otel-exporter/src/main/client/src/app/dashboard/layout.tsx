"use client";

import React from "react";
import { MdDashboard } from "react-icons/md";
import { SiLoop } from "react-icons/si";
import { IoSettingsOutline } from "react-icons/io5";
import { RiPagesLine } from "react-icons/ri";
import { IoIosHelpCircle } from "react-icons/io";
import { TbClockHour10Filled } from "react-icons/tb";
import { useRouter, usePathname } from "next/navigation";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const router = useRouter();
  const pathname = usePathname();

  // Return active/inactive class based on the current URL path
  const isActive = (path: string) => {
    return pathname === path
      ? "bg-gray-800 text-white"
      : "hover:bg-gray-800 hover:text-white";
  };

  return (
    <div className="flex h-screen bg-black text-white">
      {/* Sidebar */}
      <aside className="w-60 bg-midnight-navy flex flex-col p-3">
        {/* Project Switcher */}
        <div className="flex items-center mb-4">
          <SiLoop />
          <span className="font-bold text-base pl-2">Prompt Chain</span>
        </div>
        <div className="mb-3">
          <div className="flex items-center gap-2 mb-1">
            <div className="bg-teal-700 rounded-full h-7 w-7 flex items-center justify-center font-bold text-sm">
              S
            </div>
            <span className="text-sm">Syed</span>
            <span className="ml-auto text-sm text-gray-400">&#9660;</span>
          </div>
        </div>

        {/* Observe */}
        <div className="mb-3">
          <div className="uppercase text-[15px] text-gray-500 mb-1">
            Observe
          </div>
          <ul>
            <li className="mb-1">
              <button
                className={`flex items-center px-2 py-1 rounded w-full cursor-pointer text-left ${isActive(
                  "/dashboard"
                )}`}
                onClick={() => router.push("/dashboard")}
              >
                <MdDashboard className="mr-2 text-base" />
                Dashboard
              </button>
            </li>
            <li className="mb-1">
              <button
                className={`flex items-center px-2 py-1 rounded w-full cursor-pointer text-left ${isActive(
                  "/dashboard/traces"
                )}`}
                onClick={() => router.push("/dashboard/traces")}
              >
                <TbClockHour10Filled className="mr-2 text-base" />
                Tracing
              </button>
            </li>
            <li className="mb-1">
              <button
                className={`flex items-center px-2 py-1 rounded w-full cursor-pointer text-left ${isActive(
                  "/dashboard/observability"
                )}`}
                onClick={() => router.push("/dashboard/observability")}
              >
                <RiPagesLine className="mr-2 text-base" />
                LLM Observability
              </button>
            </li>
          </ul>
        </div>

        {/* Settings */}
        <div className="mb-3">
          <div className="uppercase text-[15px] text-gray-500 cursor-pointer mb-1">
            Settings
          </div>
          <ul>
            <li className="mb-1">
              <button className="flex items-center cursor-pointer px-2 py-1 rounded hover:bg-gray-800 text-sm w-full text-left">
                <IoSettingsOutline className="mr-2 text-base" />
                Settings
              </button>
            </li>
            <li className="mb-1">
              <button className="flex items-center px-2 py-1 cursor-pointer rounded hover:bg-gray-800 text-sm w-full text-left">
                <IoIosHelpCircle className="mr-2 text-base" />
                Help
              </button>
            </li>
          </ul>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-8 overflow-y-auto">{children}</main>
    </div>
  );
}
