import path from "path";
import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  experimental: {
    turbopack: {
      fsAllow: [
        path.resolve(__dirname, "../resources/static/client")
      ]
    }
  }
};

export default nextConfig;
