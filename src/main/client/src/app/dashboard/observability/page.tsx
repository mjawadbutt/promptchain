"use client";

import {
  LineChart,
  Line,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

export default function DashboardPage() {
  // Mock Data
  const latencyDistribution = [
    { name: "04-13", value: 50 },
    { name: "15.04", value: 70 },
    { name: "50-60", value: 90 },
    { name: "60-1.01", value: 120 },
    { name: "1.01-500", value: 240 },
    { name: "11.01-12.00", value: 180 },
  ];

  const qualityMetrics = [
    { name: "04-13", val1: 5, val2: 4 },
    { name: "04-14", val1: 5.5, val2: 4.2 },
    { name: "04-15", val1: 6, val2: 4.3 },
    { name: "04-16", val1: 6.5, val2: 4.5 },
    { name: "04-17", val1: 7, val2: 4.6 },
    { name: "04-18", val1: 8, val2: 5 },
  ];

  const errorBreakdown = [
    { name: "Rate Limit", value: 32 },
    { name: "Tmout", value: 28 },
    { name: "Invalid JSON", value: 25 },
  ];

  return (
    <div className="p-6 bg-[#0B0E14] min-h-screen text-white">
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold">LLM Observability Dashboard</h1>
          <p className="text-gray-400 text-sm">
            Get insights on your LLM application. View latency, token usage,
            errors, and quality trends.
          </p>
        </div>
        <div className="flex items-center gap-4 text-gray-400 text-sm">
          <span>Last 1h</span>
          <span>Updated 1h ago</span>
        </div>
      </div>

      {/* Top Stats */}
      <div className="grid grid-cols-4 gap-4 mb-6">
        <Card title="Requests Today" value="7,523" sub="+6%">
          <MiniBar />
        </Card>
        <Card title="Average Latency" value="210 ms">
          <MiniLine />
        </Card>
        <Card title="Average Token Usage per Requet" value="1,320 tokens" />
        <Card title="Error Rate" value="3.2%" red>
          <MiniLine red />
        </Card>
      </div>

      {/* Middle Charts */}
      <div className="grid grid-cols-3 gap-6 mb-6">
        {/* Latency Distribution */}
        <ChartCard title="Latency Distribution">
          <ResponsiveContainer width="100%" height={200}>
            <BarChart data={latencyDistribution}>
              <CartesianGrid strokeDasharray="3 3" stroke="#1E293B" />
              <XAxis dataKey="name" stroke="#94A3B8" />
              <YAxis stroke="#94A3B8" />
              <Tooltip />
              <Bar dataKey="value" fill="#6366F1" />
            </BarChart>
          </ResponsiveContainer>
        </ChartCard>

        {/* Quality Metrics */}
        <ChartCard title="Quality Metrics">
          <ResponsiveContainer width="100%" height={200}>
            <LineChart data={qualityMetrics}>
              <CartesianGrid strokeDasharray="3 3" stroke="#1E293B" />
              <XAxis dataKey="name" stroke="#94A3B8" />
              <YAxis stroke="#94A3B8" />
              <Tooltip />
              <Line type="monotone" dataKey="val1" stroke="#8B5CF6" />
              <Line type="monotone" dataKey="val2" stroke="#3B82F6" />
            </LineChart>
          </ResponsiveContainer>
        </ChartCard>

        {/* Quality Metrics Table */}
        <TableCard
          title="Quality Metrics"
          headers={["Mege", "Avg U.", "Retpe", "Lant."]}
          rows={[
            ["req_abc123", "220 ms", "2.5", "$0.012"],
            ["req_def456", "180 ms", "3.8", "$0.004"],
          ]}
        />
      </div>

      {/* Bottom Row */}
      <div className="grid grid-cols-4 gap-6">
        {/* Error Breakdown */}
        <ChartCard title="Error Breakdown">
          <ResponsiveContainer width="100%" height={200}>
            <BarChart data={errorBreakdown}>
              <CartesianGrid strokeDasharray="3 3" stroke="#1E293B" />
              <XAxis dataKey="name" stroke="#94A3B8" />
              <YAxis stroke="#94A3B8" />
              <Tooltip />
              <Bar dataKey="value" fill="#6366F1" />
            </BarChart>
          </ResponsiveContainer>
        </ChartCard>

        {/* Quality Metrics again */}
        <ChartCard title="Quality Metrics">
          <ResponsiveContainer width="100%" height={200}>
            <LineChart data={qualityMetrics}>
              <CartesianGrid strokeDasharray="3 3" stroke="#1E293B" />
              <XAxis dataKey="name" stroke="#94A3B8" />
              <YAxis stroke="#94A3B8" />
              <Tooltip />
              <Line type="monotone" dataKey="val1" stroke="#8B5CF6" />
              <Line type="monotone" dataKey="val2" stroke="#3B82F6" />
            </LineChart>
          </ResponsiveContainer>
        </ChartCard>

        {/* Model Comparison Table */}
        <TableCard
          title="Model Comparison"
          headers={["Model", "Ava Latency", "Error"]}
          rows={[
            ["gpt-4", "220 ms", "2.5%"],
            ["gpt-3.5 turbo", "150 ms", "3.0%"],
          ]}
        />

        {/* Top Expensive Requests */}
        <TableCard
          title="Top Expensive Requests"
          headers={["Req", "Tokens Usd", "Lancy"]}
          rows={[
            ["req_abc123", "5200", "4.57"],
            ["req_def456", "4600", "4.50"],
            ["req_ghf769", "4500", "4.50"],
          ]}
        />
      </div>
    </div>
  );
}

/* Components */
function Card({
  title,
  value,
  sub,
  children,
  red,
}: {
  title: string;
  value: string;
  sub?: string;
  children?: React.ReactNode;
  red?: boolean;
}) {
  return (
    <div className="bg-[#111827] rounded-lg p-4">
      <p className="text-gray-400 text-sm">{title}</p>
      <p className={`text-xl font-semibold ${red ? "text-red-400" : ""}`}>
        {value}
      </p>
      {sub && <p className="text-green-500 text-sm">{sub}</p>}
      {children && <div className="mt-2">{children}</div>}
    </div>
  );
}

function ChartCard({
  title,
  children,
}: {
  title: string;
  children: React.ReactNode;
}) {
  return (
    <div className="bg-[#111827] rounded-lg p-4">
      <p className="text-gray-400 text-sm mb-2">{title}</p>
      {children}
    </div>
  );
}

function TableCard({
  title,
  headers,
  rows,
}: {
  title: string;
  headers: string[];
  rows: string[][];
}) {
  return (
    <div className="bg-[#111827] rounded-lg p-4">
      <p className="text-gray-400 text-sm mb-2">{title}</p>
      <table className="w-full text-sm">
        <thead>
          <tr className="text-gray-400">
            {headers.map((h) => (
              <th key={h} className="text-left pb-2">
                {h}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row, i) => (
            <tr key={i} className="border-t border-gray-800">
              {row.map((cell, j) => (
                <td key={j} className="py-1">
                  {cell}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

// Small inline demo charts
function MiniBar() {
  const data = [
    { name: "1", value: 3 },
    { name: "2", value: 4 },
    { name: "3", value: 5 },
    { name: "4", value: 6 },
    { name: "5", value: 8 },
  ];
  return (
    <ResponsiveContainer width="100%" height={40}>
      <BarChart data={data}>
        <Bar dataKey="value" fill="#6366F1" />
      </BarChart>
    </ResponsiveContainer>
  );
}

function MiniLine({ red }: { red?: boolean }) {
  const data = [
    { name: "1", value: 2.5 },
    { name: "2", value: 2.8 },
    { name: "3", value: 3.0 },
    { name: "4", value: 3.1 },
  ];
  return (
    <ResponsiveContainer width="100%" height={40}>
      <LineChart data={data}>
        <Line
          type="monotone"
          dataKey="value"
          stroke={red ? "#F87171" : "#3B82F6"}
          strokeWidth={2}
          dot={false}
        />
      </LineChart>
    </ResponsiveContainer>
  );
}
