// app/page.tsx
export default function Page() {
  const traces = [
    {
      status: "success",
      startTime: "2 min ago",
      latency: 1120,
      traceId: "b3bfc4f3b29\nd30c42b3f4\nf22d9a9f4d",
      name: "LLM Prompt: Summarize User Query",
      vendor: "openai",
      tokens: 523,
      tokensOutput: 128,
    },
    {
      status: "success",
      startTime: "2 min ago",
      latency: 400,
      traceId: "660c63257de\n34c92",
      name: "Embedding Generation",
      vendor: "openai",
      tokens: 80,
      tokensOutput: "—",
    },
    {
      status: "success",
      startTime: "2 min ago",
      latency: 1120,
      traceId: "b3bfc4f3b29\nd90c42b3f4\nf22d9a9f4d",
      name: "openai",
      vendor: "openai",
      tokens: "gpt-4",
      tokensOutput: "text-embedding-ada-002",
    },
  ];

  return (
    <div className="min-h-screen bg-[#0b0f19] text-white p-8">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold">Traces</h1>
        <p className="text-sm text-gray-400 mt-1">
          service.name = <span className="text-white">ai-chat-service</span>
        </p>
        <p className="text-sm text-gray-400">
          service.version = <span className="text-white">1.3.2</span>
        </p>
        <p className="text-sm text-gray-400">
          deployment.environment ={" "}
          <span className="text-green-400 font-medium">production</span>
        </p>
      </div>

      {/* Table */}
      <div className="mt-6 overflow-x-auto">
        <table className="w-full text-sm">
          <thead className="text-gray-400 border-b border-gray-700">
            <tr>
              <th className="py-3 text-left font-medium">Status</th>
              <th className="py-3 text-left font-medium">Start time</th>
              <th className="py-3 text-left font-medium">Latency (ms)</th>
              <th className="py-3 text-left font-medium">Trace ID</th>
              <th className="py-3 text-left font-medium">Name</th>
              <th className="py-3 text-left font-medium">Vendor</th>
              <th className="py-3 text-left font-medium">Tokens</th>
              <th className="py-3 text-left font-medium">Tokens Output</th>
            </tr>
          </thead>
          <tbody>
            {traces.map((trace, idx) => (
              <tr
                key={idx}
                className="border-b border-gray-800 hover:bg-midnight-navy transition-colors"
              >
                <td className="py-3">
                  <span className="inline-block w-3 h-3 rounded-full bg-green-500"></span>
                </td>
                <td className="py-3">{trace.startTime}</td>
                <td className="py-3">{trace.latency.toLocaleString()}</td>
                <td className="py-3 whitespace-pre">{trace.traceId}</td>
                <td className="py-3">{trace.name}</td>
                <td className="py-3">{trace.vendor}</td>
                <td className="py-3">{trace.tokens}</td>
                <td className="py-3">{trace.tokensOutput}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
